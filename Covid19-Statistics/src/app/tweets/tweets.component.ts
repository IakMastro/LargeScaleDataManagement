import { Component, OnInit } from '@angular/core';
import { Tweet } from '../tweet';
import { TweetDataService } from '../tweet-data.service';

@Component({
  selector: 'app-tweets',
  templateUrl: './tweets.component.html',
  styleUrls: ['./tweets.component.sass']
})
export class TweetsComponent implements OnInit {
  tweets: Tweet[] = []

  constructor(private tweetService: TweetDataService) { }

  ngOnInit(): void {
    this.getTweets()
  }

  getTweets(): void {
    this.tweetService.getTweets().subscribe(tweets => this.tweets = tweets)
  }
}
