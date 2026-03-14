import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberProfilePopUp } from './member-profile-pop-up';

describe('MemberProfilePopUp', () => {
  let component: MemberProfilePopUp;
  let fixture: ComponentFixture<MemberProfilePopUp>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberProfilePopUp]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MemberProfilePopUp);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
