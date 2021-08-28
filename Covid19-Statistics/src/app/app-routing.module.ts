import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StatisticsComponent } from './statistics/statistics.component';
import { TweetsComponent } from './tweets/tweets.component';

const routes: Routes = [
  { path: '', redirectTo: '/tweets', pathMatch: 'full' },
  { path: 'tweets', component: TweetsComponent },
  { path: 'statistics', component: StatisticsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
