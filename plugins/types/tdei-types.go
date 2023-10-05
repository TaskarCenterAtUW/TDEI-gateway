package tdeitypes

type Versions struct {
	Versions []Version `json:"versions"`
}
type Version struct {
	Version       string `json:"version"`
	Documentation string `json:"documentation"`
	Specification string `json:"specification"`
}

type PluginConfig struct {
	ApiKeyHeader          string
	AuthServer            string
	PassThroughUrls       []string
	ApiDcoumentationUrl string
	ApiSpecificationUrl string
}
