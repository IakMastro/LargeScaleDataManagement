import { Filter } from "./filter";
import { Language } from "./language";
import { Verified } from "./verified";
import { Location } from "./location";

export interface Statistics {
    filters: Filter[]
    languages: Language[]
    locations: Location[]
    verified: Verified[]
}