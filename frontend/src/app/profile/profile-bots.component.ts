import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {BotsListConfig, Profile} from '../core';

@Component({
  selector: 'app-profile-bots',
  templateUrl: './profile-bots.component.html'
})
export class ProfileBotsComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
  ) {}

  botsListConfig: BotsListConfig[];

  ngOnInit() {
    this.route.parent.data.subscribe(
      (data: {profile: Profile}) => {
          this.botsListConfig = data.profile.botsList;
      }
    );
  }

}
