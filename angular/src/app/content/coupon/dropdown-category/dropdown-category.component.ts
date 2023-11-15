import {
  Component,
  ElementRef,
  forwardRef,
  HostListener,
  Input,
  ViewEncapsulation
} from '@angular/core';
import {
  ControlValueAccessor,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  NG_VALUE_ACCESSOR,
} from "@angular/forms";
import {userMatch} from "../../../directive/form-validator.directive";

@Component({
  selector: 'app-dropdown-category',
  templateUrl: './dropdown-category.component.html',
  styleUrls: ['./dropdown-category.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DropdownCategoryComponent),
      multi: true
    }
  ],
})
export class DropdownCategoryComponent
  implements ControlValueAccessor {

  @Input()
  tagsForm!: FormGroup;
  internalValue: string[] = [];
  isOpen: boolean = false;
  newTag: string = '';

  constructor(
    private elementRef: ElementRef,
    private formBuilder: FormBuilder) {
  }

  onChange = (_: any): void => {
  };
  onTouched = (): void => {
  };

  writeValue(value: string[]): void {
    this.internalValue = value || [];
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  @HostListener('click')
  toggleTag(tag: string): void {
    const index = this.internalValue.indexOf(tag);
    if (index >= 0) {
      this.internalValue.splice(index, 1);
    } else {
      this.internalValue.push(tag);
    }
    this.onChange(this.internalValue);
    this.onTouched();
  }

  toggleOpen() {
    this.isOpen = !this.isOpen;
  }

  public getControls(control: FormGroup, path: string): FormControl[] {
    return (control.get(path) as FormArray)
      .controls as FormControl[];
  }

  public addTag(): void {
    if (this.newTag.trim() !== '' && /^[A-Za-z]+$/.test(this.newTag)) {
      const tagsArray: FormArray
        = this.tagsForm.get('tags') as FormArray;
      (this.tagsForm.get('tags') as FormArray)
      .insert(0, this.formBuilder.control(this.newTag, userMatch));
      this.newTag = '';
      this.onChange(tagsArray.value);
    }
  }

  removeTag(index: number): void {
    this.onChange(this.internalValue);
    (this.tagsForm.get('tags') as FormArray)
    .removeAt(index);
  }

  @HostListener('window:keyup', ['$event'])
  @HostListener('document:click', ['$event'])
  onEvent(event: KeyboardEvent | Event): void {
    if (event instanceof KeyboardEvent && event.key === 'Escape') {
      this.isOpen = false;
    } else if (event instanceof Event) {
      const target: HTMLElement = event.target as HTMLElement;
      if (this.isOpen && !this.elementRef.nativeElement.contains(target)) {
        this.isOpen = false;
      }
    }
  }
}
