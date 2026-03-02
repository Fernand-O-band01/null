import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSettingsModal } from './user-settings-modal';

describe('UserSettingsModal', () => {
  let component: UserSettingsModal;
  let fixture: ComponentFixture<UserSettingsModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSettingsModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserSettingsModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
