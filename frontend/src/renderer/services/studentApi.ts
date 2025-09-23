import axios, { AxiosResponse } from 'axios';
import { Student, ApiResponse } from '../types/Student';

const API_BASE_URL = 'http://localhost:8080/api';

class StudentApi {
  private api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  async getAllStudents(): Promise<ApiResponse<Student[]>> {
    const response: AxiosResponse<ApiResponse<Student[]>> = await this.api.get('/students');
    return response.data;
  }

  async getStudentById(id: string): Promise<ApiResponse<Student>> {
    const response: AxiosResponse<ApiResponse<Student>> = await this.api.get(`/students/${id}`);
    return response.data;
  }

  async addStudent(student: Student): Promise<ApiResponse<Student>> {
    const response: AxiosResponse<ApiResponse<Student>> = await this.api.post('/students', student);
    return response.data;
  }

  async updateStudent(id: string, student: Student): Promise<ApiResponse<Student>> {
    const response: AxiosResponse<ApiResponse<Student>> = await this.api.put(`/students/${id}`, student);
    return response.data;
  }

  async deleteStudent(id: string): Promise<ApiResponse<void>> {
    const response: AxiosResponse<ApiResponse<void>> = await this.api.delete(`/students/${id}`);
    return response.data;
  }
}

export const studentApi = new StudentApi();