import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StreamerDashboardComponent } from './streamer-dashboard.component';

describe('StreamerDashboardComponent', () => {
  let component: StreamerDashboardComponent;
  let fixture: ComponentFixture<StreamerDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StreamerDashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StreamerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
