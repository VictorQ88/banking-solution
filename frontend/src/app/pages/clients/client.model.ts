export type Client = {
  id: number;
  name: string;
  gender: string;
  age: number;
  identification: string;
  address: string;
  phone: string;
  clientId: string;
  active: boolean;
};

export type ClientCreateRequest = {
  name: string;
  gender: string;
  age: number;
  identification: string;
  address: string;
  phone: string;
  password: string;
  active: boolean;
};

export type ClientUpdateRequest = ClientCreateRequest;