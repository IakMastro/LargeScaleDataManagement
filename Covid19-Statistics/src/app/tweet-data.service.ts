import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Statistics } from './statistics';
import { Tweet } from './tweet';

@Injectable({
  providedIn: 'root'
})
export class TweetDataService {
  private url: string = "http://localhost:8080/"

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  }

  constructor(private http: HttpClient) {}

  getTweets(): Observable<Tweet[]> {
    return this.http.get<Tweet[]>(this.url.concat('data'))
  }

  getStatistics(): Observable<Statistics> {
    return this.http.get<Statistics>(this.url)
  }
}
