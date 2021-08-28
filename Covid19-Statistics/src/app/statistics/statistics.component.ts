import { Component, OnInit } from '@angular/core';
import { Filter } from '../filter';
import { Language } from '../language';
import { TweetDataService } from '../tweet-data.service';
import { Verified } from '../verified';
import { Location } from "../location";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.sass']
})
export class StatisticsComponent implements OnInit {
  filters: Filter[] = []
  languages: Language[] = []
  locations: Location[] = []
  verified: Verified[] = []

  constructor(private tweetService: TweetDataService) { }

  ngOnInit(): void {
    this.getStatistics()
  }

  getStatistics(): void {
    this.tweetService.getStatistics().subscribe(statistics => {
      this.filters = statistics.filters
      this.languages = statistics.languages
      this.locations = statistics.locations
      this.verified = statistics.verified
    })
  }
}
