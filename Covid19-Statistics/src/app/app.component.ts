import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'Covid 19 Statistics';

  toggleMobileMenu(): void {
    // @ts-ignore 
    document.querySelector('#menu').classList.toggle('active')
    // @ts-ignore
    document.querySelector('.mobile-bar').classList.toggle('active')
  }
}
