import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPanelPopUp } from './user-panel-pop-up';

describe('UserPanelPopUp', () => {
  let component: UserPanelPopUp;
  let fixture: ComponentFixture<UserPanelPopUp>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserPanelPopUp]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserPanelPopUp);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
