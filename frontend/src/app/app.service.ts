import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { map } from 'rxjs/operators';
import { Observable } from 'rxjs/index';

/**
 * Class representing application service.
 *
 * @class AppService.
 */
@Injectable()
export class AppService {
  private serviceUrl = '/api/message';
  private dataPostTestUrl = '/api/message';

  constructor(private http: HttpClient) {
  }

  /**
   * Makes a http get request to retrieve the welcome message from the backend service.
   */
  public getWelcomeMessage() {
    return this.http.get(this.serviceUrl).pipe(
      map(response => response)
    );
  }

  /**
   * Makes a http post request to send some data to backend & get response.
   */
  public sendData(): Observable<any> {
    return this.http.post(this.dataPostTestUrl, {
      "id": "8b5c8ee9-01d4-480f-aa93-3a9c1a8bc1ab",
      "messageId": "e6a03080-060e-4d13-9f2d-d143c31d64e2",
      "body": "asdasd",
      "created": "2021-09-05T16:02:55.472482Z"
    });
  }
}
