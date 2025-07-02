import { Panel } from "../get-dashboard.service";

export interface ViewPanel {
    id: number;
  title: string;
  type: string;
  chartOptions?: any;
  gaugeValue?: number;
  raw: Panel;
}
