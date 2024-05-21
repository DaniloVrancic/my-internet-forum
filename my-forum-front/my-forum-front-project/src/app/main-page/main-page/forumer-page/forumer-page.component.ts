import { Component, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumerPageService } from '../../../services/forumer-page.service';
import { TopicService } from '../../../services/topic.service';
import { Topic } from '../../../../interfaces/topic';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forumer-page',
  standalone: true,
  imports: [NavigationBarComponent],
  templateUrl: './forumer-page.component.html',
  styleUrl: './forumer-page.component.css',
  providers: [ForumerPageService, TopicService]
})
export class ForumerPageComponent implements OnInit{


  allTopics: Topic[];

  constructor(private forumerPageService: ForumerPageService, private topicService: TopicService,
              private router: Router
  ){
    this.allTopics = [];
  }

  ngOnInit(): void {
      this.topicService.findAllTopics().subscribe({
        next: response => {this.allTopics = response; console.log(response)
        error: (error:any) => {console.error(error);}
        }
      })
  }

  setTopicIdAndRedirect(numberForSet: number) {
    this.forumerPageService.setSelectedTopicId(numberForSet);
    this.router.navigate(["/selected-topic"]);
    }



}
