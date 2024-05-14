import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { LoginRequest } from '../../../interfaces/requests/login-request';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css',
  providers: [UserService]
})
export class LoginPageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  public loginRequest: LoginRequest;
  constructor(private userService: UserService)
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

  }

}
