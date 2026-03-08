export interface VoiceJoinRequest {
  channelId: number;
  userId: number;
  username: string;
  imageUrl: string;
}

export interface VoiceParticipant {
  userId: number;
  username: string;
  imageUrl: string;
}