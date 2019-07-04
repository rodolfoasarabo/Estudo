# iFood Design System

Components for iFood Android

## Download

Gradle:
```gradle
dependencies {
	implementation 'br.com.ifood.design:design-system:0.1.5'
}
```

## Documentation

## Components
	* Snackbar:
		```
		Snackbar(context).apply {
        	message = message
        	type = type
        	duration = duration
        	anchor = anchor
    	}.show()
		```
		Types:
		```
		enum class Type {
    		SUCCESS,
    		ERROR,
    		WARNING,
    		COMMUNICATION
		}
		```