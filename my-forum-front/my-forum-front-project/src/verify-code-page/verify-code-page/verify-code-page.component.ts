import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-code-page',
  standalone: true,
  imports: [],
  templateUrl: './verify-code-page.component.html',
  styleUrl: './verify-code-page.component.css',
  providers: [Router]
})
export class VerifyCodePageComponent implements OnInit{

  private myForm : any;
  public isMyFormValid : boolean = false;
  public errorMessage: string = "";
  codeInputElements: any[] = [];
  @ViewChildren('codeInput') codeInputs!: QueryList<ElementRef>;

  constructor(private router: Router)
  {

  }

  ngOnInit(): void {
      let codeInputElements = this.codeInputs.toArray();

     
  }

  onSubmit() {
    const code = this.codeInputs.join('');
    console.log(code);
  }

  resendCode(){

  }

  


}
