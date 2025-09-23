import axios, { AxiosResponse } from 'axios';
import { ApiResponse } from '../types/Student';

const API_BASE_URL = 'http://localhost:8080/api';

class StatisticsApi {
  private api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  async getSummary(): Promise<ApiResponse<any>> {
    const response: AxiosResponse<ApiResponse<any>> = await this.api.get('/statistics/summary');
    return response.data;
  }

  async getDistribution(): Promise<ApiResponse<Record<string, number>>> {
    const response: AxiosResponse<ApiResponse<Record<string, number>>> = await this.api.get('/statistics/distribution');
    return response.data;
  }

  async getGenderStatistics(): Promise<ApiResponse<Record<string, any>>> {
    const response: AxiosResponse<ApiResponse<Record<string, any>>> = await this.api.get('/statistics/by-gender');
    return response.data;
  }

  async getAverage(): Promise<ApiResponse<number>> {
    const response: AxiosResponse<ApiResponse<number>> = await this.api.get('/statistics/average');
    return response.data;
  }

  async getHighest(): Promise<ApiResponse<number>> {
    const response: AxiosResponse<ApiResponse<number>> = await this.api.get('/statistics/highest');
    return response.data;
  }

  async getLowest(): Promise<ApiResponse<number>> {
    const response: AxiosResponse<ApiResponse<number>> = await this.api.get('/statistics/lowest');
    return response.data;
  }
}

export const statisticsApi = new StatisticsApi();