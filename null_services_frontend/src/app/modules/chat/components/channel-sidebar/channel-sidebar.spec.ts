import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChannelSidebar } from './channel-sidebar';

describe('ChannelSidebar', () => {
  let component: ChannelSidebar;
  let fixture: ComponentFixture<ChannelSidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChannelSidebar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChannelSidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
