import { Component, Inject } from '@angular/core';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ForumerPageService } from '../../../../services/forumer-page.service';
import { CreatePostRequest } from '../../../../../interfaces/requests/create-post-request';
import { UserService } from '../../../../services/user.service';

@Component({
  selector: 'app-new-forum-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './new-forum-page.component.html',
  styleUrl: './new-forum-page.component.css',
  providers: [ForumerPageService, UserService]
})
export class NewForumPageComponent {

  public inputTitle: string;
  public inputContent: string;

  constructor(@Inject(MAT_DIALOG_DATA) public allPosts: ForumPost[],
  public dialogRef: MatDialogRef<NewForumPageComponent>,
  private router: Router,
  private userService: UserService,
  private forumerService: ForumerPageService){
    this.inputTitle = "";
    this.inputContent = "";

  }

  routeToPurchasePage() {
    this.router.navigate(["/forumer-page"]).then(() => {
      this.dialogRef.close();
    });;
    }

    addNewPost() {
      if(this.inputTitle.length == 0 || this.inputContent.length == 0){
        return;
      }

      let newForumPost : CreatePostRequest = {title: this.inputTitle, content: this.inputContent, 
        topic: this.forumerService.getSelectedTopicId() as number, user: this.userService.getCurrentUser()?.id as number};

      this.forumerService.addForumPost(newForumPost).subscribe({next: result => {this.allPosts.push(result); console.log(result);},
      error: (error:any) => {console.log(error);}});


    }

}
