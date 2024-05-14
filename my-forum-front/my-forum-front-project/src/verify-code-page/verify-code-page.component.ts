import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../app/services/user.service';
import { HttpClient } from '@angular/common/http';
import { VerifyCodeRequest } from './verify-code-request-interface';
import { error } from 'console';
import { User } from '../interfaces/user';
import { response } from 'express';
import { UserStatuses } from '../interfaces/user.statuses';

@Component({
  selector: 'app-verify-code-page',
  standalone: true,
  imports: [],
  templateUrl: './verify-code-page.component.html',
  styleUrl: './verify-code-page.component.css',
  providers: [Router, UserService]
})
export class VerifyCodePageComponent implements OnInit{

  private myForm : HTMLFormElement = {} as HTMLFormElement;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  public usernameForVerify: string;

  constructor(private router: Router, public userService : UserService) //THIS PAGE SHOULDN'T BE OPENED WITHOUT A USER BEING LOADED IN THE SESSION STORAGE
  {
    this.usernameForVerify = userService.getCurrentUser()?.username as string;
  }

  ngOnInit(): void {
      this.myForm = document.forms[0];
      
      if(this.userService.getCurrentUser() == null)
        {
          this.router.navigate(['']);
        }
      else if(this.userService.getCurrentUser()?.status == UserStatuses.active || this.userService.getCurrentUser()?.status == UserStatuses.requested)
        {
          this.router.navigate(['main-page']);
        }
  }


submitCode() {
  if(this.myForm.checkValidity())
    {
      let currentUser : User = this.userService.getCurrentUser() as User;
      let codeToSubmit = this.myForm["verificationCode"].value;

      let requestToSend: VerifyCodeRequest = {user_id: currentUser?.id as number, verificationCode: codeToSubmit};

      console.log(requestToSend);

      this.userService.verifyUser(requestToSend).subscribe(response =>
        {
          this.userService.setCurrentUser(response);
          this.errorMessage = "";
          console.log(response);
        }
      ),
        (error: any) => {
        this.errorMessage = "Invalid code.";
      };
    }
  else{
    this.errorMessage = "The verification code input incorrectly."
  }
}

resendCode() {
  let currentUser = this.userService.getCurrentUser();
  let currentUserId = currentUser?.id as number;

  if(currentUser == null)
    {
      alert("No user found in session.");
    }
  else{
    this.userService.resendVerificationCodeForUserId(currentUserId).subscribe(
      (response: any) => {
        alert(response.sendingMessage);
      }
    );
  }
}

private setErrorMessage(errorMsg: string)
{
  this.errorMessage = errorMsg;
}

  


}
