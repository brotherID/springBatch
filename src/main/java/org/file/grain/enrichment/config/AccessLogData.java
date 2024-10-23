package org.file.grain.enrichment.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogData {
	private String userName;
	private String timestamp;

}
