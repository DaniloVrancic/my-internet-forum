import { TestBed } from '@angular/core/testing';

import { OauthGithubService } from './oauth-google.service';

describe('OauthGithubService', () => {
  let service: OauthGithubService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OauthGithubService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
