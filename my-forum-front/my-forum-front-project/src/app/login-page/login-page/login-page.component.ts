import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { LoginRequest } from '../../../interfaces/requests/login-request';
import { User } from '../../../interfaces/user';
import { Router } from '@angular/router';
import { UserStatuses } from '../../../interfaces/user.statuses';
import { NavigationBarComponent } from '../../partials/nav/navigation-bar/navigation-bar.component';
import { environment } from '../../../environments/environment';
import { OauthGoogleService } from '../../services/oauth-google.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  providers: [UserService, Router]
})
export class LoginPageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  public loginRequest: LoginRequest;

  public foundUrl: string;

  constructor(private userService: UserService,
              private router: Router,
              private oAuthService: OauthGoogleService)
  {
    this.loginRequest = {} as LoginRequest;
    this.foundUrl = "";
  }

  ngOnInit(): void {
      this.myForm = document.forms[0];
      this.oAuthService.get("/auth/oauth2").subscribe((data: any) => {
        this.foundUrl = data.url;
      });
  }

  onInput(){
    this.isMyFormValid = this.myForm.checkValidity();
  }

  onInputUsername(event: any){
    this.onInput();
    this.loginRequest.username = event.target.value;
  }

  onInputPassword(event: any){
    this.onInput();
    this.loginRequest.password = event.target.value;
  }

  loginUser(){
    if(!this.myForm.checkValidity)
      {
        this.errorMessage = "Not all data is correctly input.";
        return;
      }
    else{
      this.errorMessage = "";
    }

    //CONTINUE METHOD HERE (MAKE /login endpoint)
    this.userService.loginUser(this.loginRequest)
    .subscribe(
    {
    next: response => {
      let caughtToken : string = response.token;
      let userToSet: User = {id: response.id, username: response.username,
                            email: response.email, createTime: response.createTime,
                            status: response.status, type: response.type
      }
      this.userService.setCurrentUser(userToSet);
      this.userService.setJwtToken(caughtToken);
      this.errorMessage = ""; // Remove the error message at this point cause everything went alright.

      sessionStorage.setItem(environment.needsReloadString, "true");

      if(response.status == UserStatuses.requested)
        {
          this.router.navigate(["/verify-page"]); //redirect the user to verification if he hasn't already been verified
          alert("Please verify your account.\nVerification code has been sent to your e-mail.")
        }
      else{
        this.router.navigate(["/main-page"]);
      }
    }, 
    error: error => {
      if(error.status === 403)
        {
          this.errorMessage = "User is blocked.";
        }
      else if(error.status === 404){
        this.errorMessage = "Incorrect user data.";
      }
      else{
          this.errorMessage = "Something went wrong.";
      }
      console.error(this.errorMessage);
    },
    complete: () => {}
    });

  }

  loginUserGithub() {
    
    }

}
