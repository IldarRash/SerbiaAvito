import { Injectable, } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import {Profile, ProfilesService, User, UserService} from '../core';
import { catchError } from 'rxjs/operators';

@Injectable()
export class ProfileResolver implements Resolve<User> {
  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<any> {

    return Observable.apply(this.userService.getCurrentUser());

  }
}
