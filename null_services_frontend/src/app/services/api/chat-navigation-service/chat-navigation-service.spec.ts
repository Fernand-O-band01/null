import { TestBed } from '@angular/core/testing';

import { ChatNavigationService } from './chat-navigation-service';

describe('ChatNavigatioService', () => {
  let service: ChatNavigationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChatNavigationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
