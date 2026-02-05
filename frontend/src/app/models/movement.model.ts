export interface Movement {
  id: number;
  movementDate: string;
  movementType: 'DEPOSIT' | 'WITHDRAWAL';
  value: number;
  balance: number;
  accountId: number;
}
