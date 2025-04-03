import { Component } from '@angular/core';
import { NetworkNode, ScannerService } from 'src/app/services/scanner.service';

@Component({
  selector: 'app-network-scanner',
  templateUrl: './network-scanner.component.html',
  styleUrls: ['./network-scanner.component.scss']
})
export class NetworkScannerComponent {
  subnet: string = '';
  nodes: NetworkNode[] = [];

  constructor(private scannerService: ScannerService) {}

  scan() {
    if (!this.subnet) {
      alert('Veuillez entrer un subnet valide.');
      return;
    }

    this.scannerService.scanNetwork(this.subnet).subscribe({
      next: (data) => {
        this.nodes = data;
        alert(data);
        
      },
      error: (error) => {
        console.error('Erreur lors du scan', error);
        alert('Erreur lors du scan. Vérifiez votre API.');
      }
    });
  }
}
