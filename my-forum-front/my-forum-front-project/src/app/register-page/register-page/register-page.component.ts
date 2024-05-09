import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterRequest } from './registerRequest';
import { Form } from '@angular/forms';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [ HttpClientModule ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.css',
  providers: [Router]
})
export class RegisterPageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  constructor(private router: Router,
            private http: HttpClient
            ){
              
            }

  ngOnInit(): void {
      this.myForm = document.forms[0];
  }

onInput(){
  if(this.myForm.checkValidity())
    {
      this.isMyFormValid = true;
    }
  else
  {
    this.isMyFormValid = false;
  }
}


registerUser() {
  
  
  const formData = new FormData();
  
  let registerRequest : RegisterRequest = {} as RegisterRequest;

  console.log(this.myForm);

  /*
  formData.append('username', this.username);
  formData.append('password', this.password);
  formData.append('email', this.email);
  

  this.http.post('your-api-endpoint', formData)
    .subscribe(
    {
    next: response => {
      // Handle success response
    }, 
    error: error => {},
      // Handle error response
    complete: () => {}
    });
    */
}

}
