import { Component } from '@angular/core';

import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: string | undefined;
  postRequestResponse: string | undefined;

  constructor(private appService: AppService) {
    this.appService.getWelcomeMessage().subscribe((data: any) => {
      console.log(data)
      this.title = data.created;
    });
  }

  /**
   * This method is used to test the post request
   */
  public postData(): void {
    this.appService.sendData().subscribe((data: any) => {
      console.log(data)
      this.postRequestResponse = data;
    });
  }
}
