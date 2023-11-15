import {Component, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'app-tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TagsComponent {
  tags: string[] = ['Cosmetics', 'Makeup', 'Travel', 'Celebration', 'Culture', 'Holiday'];

  newTag: string = '';
  selectedColor: string = '#6C7';
  colors: string[] = ['#F95', '#6AD', '#E77', '#6C7', '#AAA'];

  addTag(): void {
    if (this.newTag.length > 0) {
      this.tags.push(this.newTag);
      this.newTag = '';
    }
  }

  removeTag(tag: string): void {
    const index = this.tags.indexOf(tag);
    if (index !== -1) {
      this.tags.splice(index, 1);
    }
  }

  changeColor(color: string): void {
    this.selectedColor = color;
  }
}
