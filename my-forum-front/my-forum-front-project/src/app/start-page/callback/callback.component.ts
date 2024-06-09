import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OauthGoogleService } from '../../services/oauth-google.service';

@Component({
  selector: 'app-callback',
  standalone: true,
  imports: [],
  templateUrl: './callback.component.html',
  styleUrl: './callback.component.css'
})
export class CallbackComponent implements OnInit{
  constructor(private route: ActivatedRoute, private router: Router, private OAuthService: OauthGoogleService){}

  ngOnInit(): void {
      const token : string = this.route.snapshot.queryParamMap.get('code') as string;

      console.log(token);

      if(token !== undefined){
        this.OAuthService.getToken(token).subscribe(result => {
          if(result == true) {
            console.log("CAN LOG IN NOW");
            console.log(result);
          }
          else{
            console.log("CAN'T LOGIN NOW");
            console.log(result);
          }
        });
      }

  }
}
