import axios, { AxiosResponse } from 'axios';
import { ApiResponse } from '../types/Student';

const API_BASE_URL = 'http://localhost:8080/api';

class CsvApi {
  private api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 30000, // Longer timeout for file operations
  });

  async exportToCsv(filename?: string): Promise<Blob> {
    const response: AxiosResponse<Blob> = await this.api.get('/csv/export', {
      params: { filename },
      responseType: 'blob',
    });
    return response.data;
  }

  async importFromCsv(file: File, append: boolean = false): Promise<ApiResponse<string>> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('append', append.toString());

    const response: AxiosResponse<ApiResponse<string>> = await this.api.post('/csv/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  }

  async getStatus(): Promise<ApiResponse<string>> {
    const response: AxiosResponse<ApiResponse<string>> = await this.api.get('/csv/status');
    return response.data;
  }
}

export const csvApi = new CsvApi();