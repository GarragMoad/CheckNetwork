import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { GuacamoleService } from 'src/app/services/guacamole.service';

@Component({
  selector: 'app-guacamole',
  templateUrl: './guacamole.component.html',
  styleUrls: ['./guacamole.component.scss']
})
export class GuacamoleComponent {
  username = '';
  password = '';
  connections: any[] = [];
  connectionUrl: SafeResourceUrl | null = null;

  constructor(private guacService: GuacamoleService, private sanitizer: DomSanitizer) {}

}