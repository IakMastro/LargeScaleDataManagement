import { TestBed } from '@angular/core/testing';

import { TweetDataService } from './tweet-data.service';

describe('TweetDataService', () => {
  let service: TweetDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TweetDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
