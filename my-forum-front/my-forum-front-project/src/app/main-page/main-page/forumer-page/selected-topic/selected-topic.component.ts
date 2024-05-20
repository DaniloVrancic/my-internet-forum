import { Component } from '@angular/core';
import { ForumerPageService } from '../../../../services/forumer-page.service';

@Component({
  selector: 'app-selected-topic',
  standalone: true,
  imports: [],
  templateUrl: './selected-topic.component.html',
  styleUrl: './selected-topic.component.css',
  providers: [ForumerPageService]
})
export class SelectedTopicComponent {

  constructor(private forumerPageService: ForumerPageService){
    
  }

}
