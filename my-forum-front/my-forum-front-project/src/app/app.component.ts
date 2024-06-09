import { JsonPipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { OauthGoogleService } from './services/oauth-google.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, JsonPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'my-forum-front-project';
  token: string = "";

  constructor(private router: Router,
    private activateRoute: ActivatedRoute, private http: OauthGoogleService
  ){}

  ngOnInit(): void {

  }
}
