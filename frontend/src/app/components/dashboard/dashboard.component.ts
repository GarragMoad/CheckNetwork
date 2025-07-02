import {
  ChangeDetectionStrategy,
  Component,
  OnInit
} from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexYAxis
} from 'ng-apexcharts';
import { Panel } from 'src/app/services/get-dashboard.service';
import { GrafanaServiceJson } from 'src/app/services/grafana/grafana.service';

interface ViewPanel {
  id: number;
  title: string;
  type: string;
  chartOptions?: any;
  gaugeValue?: number;
  raw: Panel;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush // ✅ BONUS perf
})
export class DashboardComponent implements OnInit {
  dashboard: any;
  viewPanels: ViewPanel[] = [];

  constructor(private grafanaService: GrafanaServiceJson) {}

  ngOnInit(): void {
    const title = 'Network-Performance-192.168.60.2';
    this.grafanaService.getDashboardJson(title).subscribe({
      next: (data: any) => {
        this.dashboard = data.dashboard;
        this.viewPanels = this.dashboard.panels.map((panel: Panel, index: number) => {
          return {
            id: panel.title ? this.hash(panel.title) + index : index,
            title: panel.title ?? '',
            type: panel.type ?? 'unknown',
            chartOptions: panel.type === 'timeseries' ? this.buildChartOptions(panel) : undefined,
            gaugeValue: panel.type === 'gauge' ? this.getGaugeValue(panel) : undefined,
            raw: panel
          };
        });
      },
      error: (err) => console.error('Erreur dashboard:', err)
    });
  }

  private buildChartOptions(panel: Panel) {
    const series: ApexAxisChartSeries = [
      {
        name: panel.targets?.[0]?.legendFormat || panel.title,
        data: this.mockTimeSeriesData()
      }
    ];

    const chart: ApexChart = {
      type: 'line',
      height: 250
    };

    const xaxis: ApexXAxis = {
      categories: ['10:00', '10:05', '10:10', '10:15', '10:20']
    };

    const yaxis: ApexYAxis = {
      title: {
        text: panel.fieldConfig?.defaults?.unit || ''
      }
    };

    return {
      series,
      chart,
      xaxis,
      yaxis,
      title: { text: panel.title }
    };
  }

  private getGaugeValue(panel: Panel): number {
    return Math.floor(Math.random() * 100);
  }

  private mockTimeSeriesData(): number[] {
    return [10, 20, 15, 30, 25];
  }

  trackById(index: number, panel: ViewPanel) {
    return panel.id;
  }

  private hash(str: string): number {
    return Array.from(str).reduce((acc, c) => acc + c.charCodeAt(0), 0);
  }
}
