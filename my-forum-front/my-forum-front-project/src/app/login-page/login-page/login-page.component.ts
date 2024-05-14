import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { LoginRequest } from '../../../interfaces/requests/login-request';
import { User } from '../../../interfaces/user';
import { Router } from '@angular/router';
import { UserStatuses } from '../../../interfaces/user.statuses';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  providers: [UserService, Router]
})
export class LoginPageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  public loginRequest: LoginRequest;
  constructor(private userService: UserService,
              private router: Router)
  {
    this.loginRequest = {} as LoginRequest;
  }

  ngOnInit(): void {
      this.myForm = document.forms[0];
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
      console.log(response);
      this.userService.setCurrentUser(response);
      this.errorMessage = ""; // Remove the error message at this point cause everything went alright.

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

}
