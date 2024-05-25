import { Component, Inject } from '@angular/core';
import { ForumPost } from '../../../../../interfaces/forum-post';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-new-forum-page',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './new-forum-page.component.html',
  styleUrl: './new-forum-page.component.css'
})
export class NewForumPageComponent {

  public inputTitle: string;
  public inputContent: string;

  constructor(@Inject(MAT_DIALOG_DATA) public allPosts: ForumPost[],
  public dialogRef: MatDialogRef<NewForumPageComponent>,
  private router: Router){
    this.inputTitle = "";
    this.inputContent = "";

  }

  routeToPurchasePage() {
    this.router.navigate(["/forumer-page"]).then(() => {
      this.dialogRef.close();
    });;
    }

}
