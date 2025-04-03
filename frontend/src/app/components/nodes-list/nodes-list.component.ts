import { Component, Input } from '@angular/core';
import { NetworkNode } from 'src/app/services/scanner.service';

@Component({
  selector: 'app-nodes-list',
  templateUrl: './nodes-list.component.html',
  styleUrls: ['./nodes-list.component.scss']
})
export class NodesListComponent {
  @Input() nodes: NetworkNode[] = [];
}
