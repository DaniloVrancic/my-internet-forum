import { Component, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumerPageService } from '../../../services/forumer-page.service';
import { TopicService } from '../../../services/topic.service';
import { Topic } from '../../../../interfaces/topic';
import { Router } from '@angular/router';
import { ForumPost } from '../../../../interfaces/forum-post';

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
  allPosts: ForumPost[];


  constructor(private forumerPageService: ForumerPageService, private topicService: TopicService,
              private router: Router
  ){
    this.forumerPageService.setSelectedTopicId(null);
    this.allTopics = [];
    this.allPosts  = [];
  }

  ngOnInit(): void {
      this.topicService.findAllTopics().subscribe({
        next: response => {this.allTopics = response;},
        error: (error:any) => {console.error(error);}
        }
      )
  }

  setTopicIdAndUpdatePosts(numberForSet: number) {
    this.forumerPageService.setSelectedTopicId(numberForSet);
    this.forumerPageService.findAllByTopicId(numberForSet).subscribe({
      next: response => this.allPosts = response,
      error: errorObj => console.error(errorObj)
    })
    }

  addNewForumPost() {
      throw new Error('Method not implemented.');
      }



}
