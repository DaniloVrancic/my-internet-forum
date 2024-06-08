import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { OauthGoogleService } from '../../services/oauth-google.service';


@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [],
  templateUrl: './start-page.component.html',
  styleUrl: './start-page.component.css',
  providers: [Router, UserService]
})
export class StartPageComponent implements OnInit {

  constructor(private router: Router, private userService: UserService,
    private activateRoute: ActivatedRoute, private http: OauthGoogleService
  ){}

  ngOnInit(): void {

  }

  registerClick(){
    this.router.navigate(['/register-page']);
  }

  loginClick(){
    this.router.navigate(['/login-page']);
  }
}
