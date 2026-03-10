import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemberSidebar } from './member-sidebar';

describe('MemberSidebar', () => {
  let component: MemberSidebar;
  let fixture: ComponentFixture<MemberSidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MemberSidebar]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MemberSidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
