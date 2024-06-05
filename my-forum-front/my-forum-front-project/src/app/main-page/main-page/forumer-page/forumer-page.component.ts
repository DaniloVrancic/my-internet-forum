import { Component, OnInit } from '@angular/core';
import { NavigationBarComponent } from '../../../partials/nav/navigation-bar/navigation-bar.component';
import { ForumerPageService } from '../../../services/forumer-page.service';
import { TopicService } from '../../../services/topic.service';
import { Topic } from '../../../../interfaces/topic';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ForumPost } from '../../../../interfaces/forum-post';

import { NewForumPageComponent } from './new-forum-page/new-forum-page.component';
import { UserService } from '../../../services/user.service';
import { MatIconModule} from '@angular/material/icon';
import { SelectedPostComponent } from './selected-post/selected-post.component';
import { EditPostComponent } from './edit-post/edit-post.component';

@Component({
  selector: 'app-forumer-page',
  standalone: true,
  imports: [NavigationBarComponent, MatIconModule],
  templateUrl: './forumer-page.component.html',
  styleUrl: './forumer-page.component.css',
  providers: [ForumerPageService, TopicService, UserService]
})
export class ForumerPageComponent implements OnInit{


  allTopics: Topic[];
  allPosts: ForumPost[];

  constructor(private forumerPageService: ForumerPageService, private topicService: TopicService,
              private userService: UserService,
              private router: Router, private dialog: MatDialog
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
    this.forumerPageService.findTop20ApprovedByTopicId(numberForSet).subscribe({
      next: response => {this.allPosts = response},
      error: errorObj => console.error(errorObj)
    })
    }

  addNewForumPost() {
      this.openDialog();
      }

  openDialog(){

    const dialogRef = this.dialog.open(NewForumPageComponent, {
      width: '70%',
      maxHeight: '90vh',
      data: this.allPosts
    });

    dialogRef.afterClosed().subscribe(result => {}).unsubscribe()
  }

  openPost(caughtPost: ForumPost){
    const dialogRef = this.dialog.open(SelectedPostComponent, {
      width: '85%',
      autoFocus: false,
      maxHeight: '90vh',
      data: caughtPost
    });

    dialogRef.afterClosed().subscribe(result => {}).unsubscribe()
  }

  userIsLoggedIn() : boolean{
    let currentUser = this.userService.getCurrentUser()?.username;
    return currentUser != null && currentUser != undefined;
  }

  topicIsSelected(): boolean{
    let currentTopic = this.forumerPageService.getSelectedTopicId();

    return currentTopic != null && currentTopic != undefined;
  }

  editPost(postToEdit: ForumPost, event: Event){
    event.stopPropagation();
    const dialogRef = this.dialog.open(EditPostComponent, {
      width: '85%',
      autoFocus: false,
      maxHeight: '90vh',
      data: postToEdit
    });

    dialogRef.afterClosed().subscribe((result: ForumPost) => {
     
      this.forumerPageService.findByPostId(postToEdit.id).subscribe({next: result => {
        if (result) {
          const index = this.allPosts.findIndex(post => post.id === result.id);
          if (index !== -1) {
            this.allPosts[index] = result;
          }
        }
      },error: errorObj => {console.error(errorObj)}
      });
      

      
    });
  }

  deletePost(postToDelete: ForumPost, event: Event){
    event.stopPropagation();
    
    let acceptedDelete = confirm(`Are you sure you want to delete:\n${postToDelete.title} ?`);

    if(acceptedDelete){
      this.forumerPageService.deleteForumPost(postToDelete.id).subscribe({
        next: result => {
          if(result){
            this.allPosts = this.allPosts.filter(post => post.id !== postToDelete.id);
            alert(`Successfully deleted:\n${result.title}`);
          }
        },
        error: errorObj => {console.error(errorObj);}
      });
    }
  }

}
