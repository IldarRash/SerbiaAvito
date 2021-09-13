import {BotsListConfig} from "./bot-list-config.model";

export interface Profile {
  username: string;
  bio: string;
  image: string;
  following: boolean;
  botsList: BotsListConfig[]
}
