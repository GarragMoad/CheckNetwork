import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from 'src/app/services/auth-service.service';
import { UserDataServiceService } from 'src/app/services/user/user-data-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  rememberMe: boolean = false;
  hidePassword: boolean = true;
  loginError: string | null = null;
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthServiceService,
    private userDataService: UserDataServiceService
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const credentials = this.loginForm.value;
      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          this.authService.saveUserName(response.username);
          this.authService.loginSuccess();
          this.userDataService.userName = response.username; 
          // Redirection après login réussi
          this.router.navigate(['dashboard']); 
        },
        error: (err) => {
          console.error('Login error:', err);
          this.loginError = 'Identifiants invalides. Veuillez réessayer.';
        }
      });
    }
  }


  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

  forgotPassword() {
    console.log('Forgot password clicked');
    // Navigation vers la page de récupération de mot de passe
  }

  signUp() {
    console.log('Sign up clicked');
    // Navigation vers la page d'inscription
  }
}