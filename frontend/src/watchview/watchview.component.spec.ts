import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WatchviewComponent } from './watchview.component';

describe('WatchviewComponent', () => {
  let component: WatchviewComponent;
  let fixture: ComponentFixture<WatchviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WatchviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WatchviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
