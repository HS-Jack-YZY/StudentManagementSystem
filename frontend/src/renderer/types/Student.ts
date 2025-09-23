export interface Student {
  id: string;
  name: string;
  gender: string;
  age: number;
  score: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  error?: string;
}