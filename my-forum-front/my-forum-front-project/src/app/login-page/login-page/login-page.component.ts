import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  constructor(){}

  ngOnInit(): void {
      this.myForm = document.forms[0];
  }

  onInput(){
    this.isMyFormValid = this.myForm.checkValidity();
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

    //CONTINUE METHOD HERE

  }

}
