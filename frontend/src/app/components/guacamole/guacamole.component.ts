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

  login() {
    this.guacService.login(this.username, this.password).subscribe(data => {
      this.connections = data.connections;
      const token = data.token;

      if (this.connections.length > 0) {
        const connectionId = this.connections[0].identifier;
        this.guacService.getConnectionUrl(connectionId, token).subscribe(res => {
          this.connectionUrl = this.sanitizer.bypassSecurityTrustResourceUrl(res.url);
        });
      }
    });
  }
}