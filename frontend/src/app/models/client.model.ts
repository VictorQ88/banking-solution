export interface Client {
  id: number;
  name: string;
  gender: string;
  age: number;
  identification: string;
  address: string;
  phone: string;
  clientId: string;
  active: boolean;
  password?: string;
}
