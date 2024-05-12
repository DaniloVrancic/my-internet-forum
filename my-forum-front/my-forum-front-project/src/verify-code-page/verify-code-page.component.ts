import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../app/services/user.service';
import { HttpClient } from '@angular/common/http';
import { VerifyCodeRequest } from './verify-code-request-interface';
import { error } from 'console';
import { User } from '../interfaces/user';

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

  constructor(private router: Router, public userService : UserService){}

  ngOnInit(): void {
      this.myForm = document.forms[0];
      
  }

resendCode() {
  throw new Error('Method not implemented.');
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

public setErrorMessage(errorMsg: string)
{
  this.errorMessage = errorMsg;
}

  


}
